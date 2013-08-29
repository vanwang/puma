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

public class ResourceSpecifications {

	public static <Resource> Specification<Resource> byResourceExample(final Resource resource) {
        checkNotNull(resource, "example must not be null");

        return new Specification<Resource>() {
            @Override
            public Predicate toPredicate(Root<Resource> root, CriteriaQuery<?> query, CriteriaBuilder builder) {
                List<Predicate> predicates = newArrayList();
                
                Object resourceName = getValue(resource, "getName");
                Object resourceString = getValue(resource, "getResourceString");
                Object resourceCreateDate = getValue(resource, "getCreateDate");
                Object resourceActive = getValue(resource, "isActive");
                Object resourceSys = getValue(resource, "isSys");
                
                if(resourceName instanceof String){
                	if (isNotEmpty((String) resourceName)) {
                        predicates.add(builder.like(root.<String>get("name"),
                                pattern((String) resourceName)));
                    }
                }
                
                if(resourceString instanceof String){
                	if (isNotEmpty((String) resourceString)) {
                        predicates.add(builder.equal(root.<String>get("resourceString"),
                        		(String) resourceString));
                    }
                }
                
                if(resourceCreateDate instanceof String){
                	if (isNotEmpty((String) resourceCreateDate)) {
                        predicates.add(builder.greaterThan(root.<Date>get("createDate"),
                                (Date) resourceCreateDate));
                    }
                }
                
                if(resourceActive instanceof Boolean){
                    predicates.add(builder.equal(root.<Boolean>get("active"),
                    		(Boolean) resourceActive));
                }
                
                if(resourceSys instanceof Boolean){
                    predicates.add(builder.equal(root.<Boolean>get("sys"),
                    		(Boolean) resourceSys));
                }
                
                
                return predicates.isEmpty() ? builder.conjunction() : builder.and(toArray(predicates, Predicate.class));
            }
            
            private Object getValue(Resource resource, String attr) {
                try {
                    return ReflectionUtils.invokeMethod(resource.getClass().getMethod(attr), resource);
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
