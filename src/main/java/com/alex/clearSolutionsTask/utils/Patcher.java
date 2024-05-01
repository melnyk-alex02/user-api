package com.alex.clearSolutionsTask.utils;

import com.alex.clearSolutionsTask.entity.User;
import org.springframework.stereotype.Component;

import java.lang.reflect.Field;

@Component
public class Patcher {
    public static void userPatcher(User existingUser, User incompleteUser) throws IllegalAccessException {
        Class<?> internClass= User.class;
        Field[] internFields=internClass.getDeclaredFields();
        for(Field field : internFields){
            field.setAccessible(true);

            Object value=field.get(incompleteUser);
            if(value!=null){
                field.set(existingUser,value);
            }
            field.setAccessible(false);
        }
    }
}