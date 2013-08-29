package com.puma.core.security;

import javax.servlet.http.HttpSession;

import org.springframework.context.ApplicationEvent;
import org.springframework.context.ApplicationListener;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContext;
import org.springframework.security.web.session.HttpSessionCreatedEvent;
import org.springframework.security.web.session.HttpSessionDestroyedEvent;

import com.puma.core.domain.Member;

public class PumaApplicationSessionListener implements ApplicationListener<ApplicationEvent>{

	@Override
	public void onApplicationEvent(ApplicationEvent event) {
		if ( event instanceof HttpSessionDestroyedEvent )
		{
			HttpSessionDestroyedEvent sessionDestroyedEvent = ( HttpSessionDestroyedEvent ) event;
			HttpSession s = sessionDestroyedEvent.getSession();
			if(s == null){
				return;
			}
			SecurityContext ctx = (SecurityContext) s.getAttribute("SPRING_SECURITY_CONTEXT");
			if(ctx == null){
				return;
			}
			
			Authentication auth=ctx.getAuthentication();
			if(auth == null){
				return;
			}
			Member member = null;
			if(auth.getPrincipal()   instanceof   Member)     
	        {
				member = (Member) auth.getPrincipal();
				System.out.println("member: "+member.getEmail());
	        }
			System.out.println ( "session destroyed ");
            s.invalidate();
		}else if ( event instanceof HttpSessionCreatedEvent )
		{
			System.out.println("session creaated");
		}
		
	}

}
