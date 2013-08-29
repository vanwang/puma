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
import com.puma.core.domain.Role;

public class RoleSpecifications {

	@SuppressWarnings("hiding")
	public static <Role> Specification<Role> byRoleExample(final Role role) {
        checkNotNull(role, "example must not be null");

        return new Specification<Role>() {
            @Override
            public Predicate toPredicate(Root<Role> root, CriteriaQuery<?> query, CriteriaBuilder builder) {
                List<Predicate> predicates = newArrayList();
                
                Object roleName = getValue(role, "getName");
                Object roleDescription = getValue(role, "getDescription");
                Object roleCreateDate = getValue(role, "getCreateDate");
                Object roleActive = getValue(role, "isActive");
                Object roleSys = getValue(role, "isSys");
                
                if(roleName instanceof String){
                	if (isNotEmpty((String) roleName)) {
                        predicates.add(builder.like(root.<String>get("name"),
                                pattern((String) roleName)));
                    }
                }
                
                if(roleDescription instanceof String){
                	if (isNotEmpty((String) roleDescription)) {
                        predicates.add(builder.like(root.<String>get("description"),
                                pattern((String) roleDescription)));
                    }
                }
                
                if(roleCreateDate instanceof String){
                	if (isNotEmpty((String) roleCreateDate)) {
                        predicates.add(builder.greaterThan(root.<Date>get("createDate"),
                                (Date) roleCreateDate));
                    }
                }
                
                if(roleActive instanceof Boolean){
                    predicates.add(builder.equal(root.<Boolean>get("active"),
                    		(Boolean) roleActive));
                }
                
                if(roleSys instanceof Boolean){
                    predicates.add(builder.equal(root.<Boolean>get("sys"),
                    		(Boolean) roleSys));
                }
                
                
                return predicates.isEmpty() ? builder.conjunction() : builder.and(toArray(predicates, Predicate.class));
            }
            
            private Object getValue(Role role, String attr) {
                try {
                    return ReflectionUtils.invokeMethod(role.getClass().getMethod(attr), role);
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
