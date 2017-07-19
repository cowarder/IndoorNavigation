package com.example.asus.opticalpath;

/**
 * Vertex.class
 * description: 二维坐标结点
 * author: Zhaoguo Wang
 * Created by Zhaoguo Wang on 2017/7/19.
 */

public class Vertex {

    public Vertex(int x,int y){
        this.xCoordinate=x;
        this.yCoordinate=y;
    }

    public Vertex(){
        this.known=false;
        this.distance=0;
    }

    public Vertex[] adjVertex;      //邻接结点数组集合

    public boolean known;       //是否判定为已知的标志

    public int distance;        //起始结点到此节点的最短距离

    public Vertex previous;     //最短距离前一个结点

    public int xCoordinate;

    public int yCoordinate;

}
