package com.shimapee.ec2;

import com.amazonaws.auth.ClasspathPropertiesFileCredentialsProvider;
import com.amazonaws.regions.Region;
import com.amazonaws.regions.Regions;
import com.amazonaws.services.ec2.AmazonEC2;
import com.amazonaws.services.ec2.AmazonEC2Client;
import com.amazonaws.services.ec2.model.DescribeInstanceStatusRequest;
import com.amazonaws.services.ec2.model.DescribeInstanceStatusResult;
import com.amazonaws.services.ec2.model.DescribeInstancesRequest;
import com.amazonaws.services.ec2.model.DescribeInstancesResult;
import com.amazonaws.services.ec2.model.Instance;
import com.amazonaws.services.ec2.model.InstanceStateName;
import com.amazonaws.services.ec2.model.InstanceStatus;
import com.amazonaws.services.ec2.model.Reservation;
import com.amazonaws.services.ec2.model.StartInstancesRequest;
import com.amazonaws.services.ec2.model.StartInstancesResult;
import com.amazonaws.services.ec2.model.Tag;

public class StartInstance extends EC2InstanceFactory {
	
	@Override
	public void action(String instanceName) {
		AmazonEC2 ec2 = new AmazonEC2Client(new ClasspathPropertiesFileCredentialsProvider());
		ec2.setRegion(Region.getRegion(Regions.AP_NORTHEAST_1));
		DescribeInstancesResult descInst = ec2.describeInstances();

		String startInstId = null;

		for(Reservation reserv : descInst.getReservations()) {
			for(Instance instance : reserv.getInstances()) {
				for(Tag tag : instance.getTags()) {
					if(tag.getValue().equals(instanceName)) {
						startInstId = instance.getInstanceId();
					}
				}
			}
		}
		
		StartInstancesRequest startRequest = new StartInstancesRequest();
		
		startRequest.withInstanceIds(startInstId);
		
		@SuppressWarnings("unused")
		StartInstancesResult result = ec2.startInstances(startRequest);
		
		boolean chkStatus = true;
		while(chkStatus) {
			DescribeInstancesResult descInstanceResult = ec2.describeInstances(
					new DescribeInstancesRequest().withInstanceIds(startInstId)
					);
			
			Instance instance = descInstanceResult.getReservations().get(0).getInstances().get(0);
			String stateInstance = instance.getState().getName();
				
			if(InstanceStateName.Running.toString().equalsIgnoreCase(stateInstance)) {
				
				DescribeInstanceStatusResult descInstStatusResult = ec2.describeInstanceStatus(
						new DescribeInstanceStatusRequest().withInstanceIds(
								instance.getInstanceId()
								)
						);
				InstanceStatus statuses = descInstStatusResult.getInstanceStatuses().get(0);
				String instCheck = statuses.getInstanceStatus().getStatus();
				String sysCheck = statuses.getSystemStatus().getStatus();

				if(instCheck.equals("ok") && sysCheck.equals("ok")) {
					chkStatus = false;
					System.out.println(instanceName + "を開始しました。");
					break;
				}
			}
			
			try {
				Thread.sleep(5000);
			} catch (InterruptedException e) {
				e.printStackTrace();
			}
		}

	}
}



