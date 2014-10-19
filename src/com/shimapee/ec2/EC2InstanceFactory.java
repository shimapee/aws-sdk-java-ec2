package com.shimapee.ec2;

public abstract class EC2InstanceFactory {
	public static EC2InstanceFactory getFactory(String classname) {
		EC2InstanceFactory factory = null;
		try {
			factory = (EC2InstanceFactory)Class.forName(classname).newInstance();
		} catch (ClassNotFoundException e) {
			System.err.println("クラス "+ classname + "は見つかりません。");
		} catch (InstantiationException e) {
			e.printStackTrace();
		} catch (IllegalAccessException e) {
			e.printStackTrace();
		}
		
		return factory;
	}
	
	public abstract void action(String instanceName);
	
}
