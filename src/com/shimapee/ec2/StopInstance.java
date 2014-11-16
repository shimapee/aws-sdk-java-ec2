package com.shimapee.ec2;

import com.amazonaws.auth.ClasspathPropertiesFileCredentialsProvider;
import com.amazonaws.regions.Region;
import com.amazonaws.regions.Regions;
import com.amazonaws.services.ec2.AmazonEC2;
import com.amazonaws.services.ec2.AmazonEC2Client;
import com.amazonaws.services.ec2.model.DescribeInstancesRequest;
import com.amazonaws.services.ec2.model.DescribeInstancesResult;
import com.amazonaws.services.ec2.model.Instance;
import com.amazonaws.services.ec2.model.InstanceStateName;
import com.amazonaws.services.ec2.model.Reservation;
import com.amazonaws.services.ec2.model.StopInstancesRequest;
import com.amazonaws.services.ec2.model.StopInstancesResult;
import com.amazonaws.services.ec2.model.Tag;

public class StopInstance extends EC2InstanceFactory {

	@Override
	public void action(String instanceName) {
		AmazonEC2 ec2 = new AmazonEC2Client(new ClasspathPropertiesFileCredentialsProvider());
		ec2.setRegion(Region.getRegion(Regions.AP_NORTHEAST_1));
		DescribeInstancesResult descInst = ec2.describeInstances();
		
		String stopInstId = null;
		
		for(Reservation reserv : descInst.getReservations()) {
			for(Instance instance : reserv.getInstances()) {
				for(Tag tag : instance.getTags()) {
					if(tag.getValue().equals(instanceName)) {
						stopInstId = instance.getInstanceId();
					}
				}
			}
		}
		
		StopInstancesRequest stopRequest = new StopInstancesRequest();
		
		stopRequest.withInstanceIds(stopInstId);
		
		StopInstancesResult stopResult = ec2.stopInstances(stopRequest);
		System.out.println(stopInstId);

		boolean chkStatus = true;
		while(chkStatus) {
			
			DescribeInstancesResult descInstResult = ec2.describeInstances(
					new DescribeInstancesRequest().withInstanceIds(stopInstId)
					);
		
			String stateInstance = descInstResult.getReservations().get(0).getInstances().get(0).getState().getName();
			if(InstanceStateName.Stopped.toString().equalsIgnoreCase(stateInstance)) {
				System.out.println(instanceName + "を停止しました。");
				break;
			}
			
			try {
				Thread.sleep(5000);
			} catch (InterruptedException e) {
				e.printStackTrace();
			}
			
		}
		
		
	}

}
