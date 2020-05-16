package com.example.networking;

class Mountain {
    private String name;
    private String location;
    private int size;


    public Mountain (String inName,String inLocation,int inSize){

        name=inName;
        location=inLocation;
        size=inSize;
    }
    public String getName() {
        return name;
    }
    public String getLocation() {
        return location;
    }

    public int getSize() {
        return size;
    }
    @Override
    public String toString() {
        return name;
    }
}
