package com.rogers.api.utils;

public Class UtilCode{

private Date dateFormat(String strDate){
        try{
        if(Objects.nonNull(strDate)){
        DateFormat format=new SimpleDateFormat("yyyy-MM-dd");
        return format.parse(strDate);
        }else return null;
        }catch(ParseException e){
        log.error("BirthDate format error:"+e);
        }
        return null;
        }


public static String getSessionToken() {
        return String.valueOf(System.currentTimeMillis()).substring(8, 13) + UUID.randomUUID().toString().substring(1, 10);
        }

        }