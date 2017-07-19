package com.example.asus.opticalpath;

/**
 * DirectedEdge.class
 * description: 加权有向边
 * author: Zhaoguo Wang
 * Created by Zhaoguo Wang on 2017/7/19.
 */

public class DirectedEdge {

    private final Vertex v;     //边的起点

    private final Vertex w;     //边的终点

    private final int weight;

    public DirectedEdge(Vertex V,Vertex W,int Weight){
        this.v=V;
        this.w=W;
        this.weight=Weight;
    }

    public int getWeight(){
        return this.weight;
    }

    public Vertex from(){
        return this.w;
    }

    public Vertex to(){
        return this.v;
    }

}
