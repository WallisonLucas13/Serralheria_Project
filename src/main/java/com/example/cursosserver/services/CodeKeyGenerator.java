package com.example.cursosserver.services;

import java.util.Arrays;
import java.util.List;
import java.util.Random;

public class CodeKeyGenerator {

    public String gerarKey(){

        Random random = new Random();

        String keyLine = "q,Q,w,W,e,E,r,R,t,T,y,Y,u,U,i,I,o,O,p,P,A,a,s,S,d,D,f,F,g,G,h,H,j,J,k,K,l,L,ç,Ç,z,Z,x,X,c,C,v,V,b,B,n,N,m,M,1234567890";
        List<String> list = Arrays.asList(keyLine.split(","));
        String keyFinish = "";

        for(int i=0; i<48; i++){
            keyFinish += list.get(random.nextInt(list.size()-1));
        }

        return keyFinish;
    }
}
