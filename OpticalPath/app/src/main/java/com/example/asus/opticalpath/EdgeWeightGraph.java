package com.example.asus.opticalpath;

import android.widget.EditText;

/**
 * Created by ASUS on 2017/7/19.
 */

public class EdgeWeightGraph {

    public Vertex[][] Table=new Vertex[5][5];        //；邻接表

    public void EdgeWeightGraph(){
        int [][]a=new int[5][5];
        for(int i=0;i!=5;i++){
            for(int j=0;j!=5;j++) {
                a[i][j] = MainActivity.t[i][j];
            }
        }
        int numV=0;     //可达结点个数
        for(int i=0;i!=a.length;i++) {
            for (int j = 0; j != a[0].length; j++) {        //计算可达结点个数
                if (a[i][j] == 1) {
                    numV++;
                }
            }
        }
        MainActivity.Bag=new Vertex[numV];
        numV=0;
        for(int i=0;i!=a.length;i++){
            for(int j=0;j!=a[0].length;j++) {
                if (a[i][j] == 1) {
                    this.Table[i][j].xCoordinate = i;
                    this.Table[i][j].yCoordinate = j;
                    MainActivity.Bag[numV]=this.Table[i][j];
                    numV++;
                }
            }
        }
        this.numOfVertex=numV;
    }

    private  int numOfVertex;      //结点的个数
}
