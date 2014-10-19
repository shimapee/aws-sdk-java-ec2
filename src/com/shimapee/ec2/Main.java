package com.shimapee.ec2;

import com.shimapee.ec2.EC2InstanceFactory;

public class Main {

	public static void main(String[] args) {
		
		if (args.length != 1) {
			System.out.println("クラス名を引数に入れてください。");
		}
		
		EC2InstanceFactory factory = EC2InstanceFactory.getFactory(args[0]);
		factory.action("Demo");
	}

}
