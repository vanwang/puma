package com.puma.core.security;


import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContext;
import org.springframework.security.core.context.SecurityContextHolder;

import com.puma.core.domain.Member;

public class SecurityUtils
{

    private SecurityUtils()
    {
    	
    }
    
    public static Member getAuthedMember(){
    	Member du = null;
    	SecurityContext   ctx   =   SecurityContextHolder.getContext();              
        Authentication   auth   =   ctx.getAuthentication();
        if(auth == null){
        	return du;
        }
        if(auth.getPrincipal()   instanceof   Member)     
        {
        	du = (Member) auth.getPrincipal();
        }
        
        return du;
    }
    
    /*public static User getAuthedMember(){
    	User du = null;
    	SecurityContext   ctx   =   SecurityContextHolder.getContext();              
        Authentication   auth   =   ctx.getAuthentication();
        if(auth.getPrincipal()   instanceof   User)     
        {
        	du = (User) auth.getPrincipal();
        }
        
        return du;
    }*/
    
    /*public static String digest32HexSHA1(String text)
    {
        byte sha1hash[] = null;
        MessageDigest md;
		try {
			md = MessageDigest.getInstance("SHA-1");
			sha1hash = new byte[40];
	        md.update(text.getBytes("utf-8"), 0, text.length());
	        sha1hash = md.digest();
		} catch (NoSuchAlgorithmException e) {
			e.printStackTrace();
		} catch (UnsupportedEncodingException e) {
			e.printStackTrace();
		}
        
        return StringUtils.convertToHex(sha1hash);
    }*/
    
}
