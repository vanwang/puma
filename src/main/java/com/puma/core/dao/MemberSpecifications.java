package com.puma.core.dao;

import static com.google.common.base.Preconditions.checkNotNull;
import static com.google.common.collect.Iterables.toArray;
import static com.google.common.collect.Lists.newArrayList;
import static org.apache.commons.lang.StringUtils.isNotEmpty;

import java.util.Date;
import java.util.List;

import javax.persistence.criteria.CriteriaBuilder;
import javax.persistence.criteria.CriteriaQuery;
import javax.persistence.criteria.Predicate;
import javax.persistence.criteria.Root;

import org.springframework.data.jpa.domain.Specification;
import org.springframework.util.ReflectionUtils;

import com.google.common.base.Throwables;

public class MemberSpecifications {

	public static <Member> Specification<Member> byMemberExample(final Member member) {
        checkNotNull(member, "example must not be null");

        return new Specification<Member>() {
            @Override
            public Predicate toPredicate(Root<Member> root, CriteriaQuery<?> query, CriteriaBuilder builder) {
                List<Predicate> predicates = newArrayList();
                
                Object memberName = getValue(member, "getName");
                Object memberEmail = getValue(member, "getEmail");
                Object memberCreateDate = getValue(member, "getCreateDate");
                Object memberLastConnection = getValue(member, "getLastConnect");
                Object memberActive = getValue(member, "isActive");
                Object memberSys = getValue(member, "isSys");
                
                if(memberName instanceof String){
                	if (isNotEmpty((String) memberName)) {
                        predicates.add(builder.like(root.<String>get("name"),
                                pattern((String) memberName)));
                    }
                }
                
                if(memberEmail instanceof String){
                	if (isNotEmpty((String) memberEmail)) {
                        predicates.add(builder.equal(root.<String>get("email"),
                        		(String) memberEmail));
                    }
                }
                
                if(memberCreateDate instanceof String){
                	if (isNotEmpty((String) memberCreateDate)) {
                        predicates.add(builder.greaterThan(root.<Date>get("createDate"),
                                (Date) memberCreateDate));
                    }
                }
                
                if(memberLastConnection instanceof String){
                	if (isNotEmpty((String) memberLastConnection)) {
                        predicates.add(builder.greaterThan(root.<Date>get("lastConnect"),
                                (Date) memberLastConnection));
                    }
                }
                
                if(memberActive instanceof Boolean){
                    predicates.add(builder.equal(root.<Boolean>get("active"),
                    		(Boolean) memberActive));
                }
                
                if(memberSys instanceof Boolean){
                    predicates.add(builder.equal(root.<Boolean>get("sys"),
                    		(Boolean) memberSys));
                }
                
                
                return predicates.isEmpty() ? builder.conjunction() : builder.and(toArray(predicates, Predicate.class));
            }
            
            private Object getValue(Member member, String attr) {
                try {
                    return ReflectionUtils.invokeMethod(member.getClass().getMethod(attr), member);
                } catch (Exception e) {
                    throw Throwables.propagate(e);
                }
            }

        };
    }

    static private String pattern(String str) {
        return "%" + str + "%";
    }
}
