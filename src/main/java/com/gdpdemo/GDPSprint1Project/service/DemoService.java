
package com.gdpdemo.GDPSprint1Project.service;

import com.gdpdemo.GDPSprint1Project.Home;

public interface DemoService {
	public void save(Home user);
	public boolean retrieve(Home user,String password);
}