package com.example.cursosserver.services;

import java.util.Arrays;
import java.util.List;
import java.util.Random;

public class CodeKeyGenerator {

    public String gerarKey(){

        Random random = new Random();

        String keyLine = "%,&,q,w,e,r,t,y,u,i,o,p,a,s,d,f,g,h,j,k,l,ç,x,c,v,b,n,m,.;,/,~,ç,l,o,i,p,0,9,8,7,6,5,4,3,2,1,-,=,1,!";
        List<String> list = Arrays.asList(keyLine.split(","));
        String keyFinish = "";

        for(int i=0; i<24; i++){
            keyFinish += list.get(random.nextInt(list.size()-1));
        }

        return keyFinish;
    }
}
