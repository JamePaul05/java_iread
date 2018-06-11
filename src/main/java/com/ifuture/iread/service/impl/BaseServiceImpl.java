package com.ifuture.iread.service.impl;

import com.ifuture.iread.entity.User;
import com.ifuture.iread.entity.UserDetail;
import com.ifuture.iread.repository.UserRepository;
import org.springframework.security.core.context.SecurityContextHolder;

public abstract class BaseServiceImpl {
	
	protected User getUser(UserRepository userRepository) {
		UserDetail userDetails = (UserDetail) SecurityContextHolder.getContext()
			    .getAuthentication()
			    .getPrincipal();
		User user=userRepository.findOne(userDetails.getId());
		return user;
	}

//	public abstract JSONObject setExceptionError(JSONObject jsonObject,String msg,boolean flag);
}
