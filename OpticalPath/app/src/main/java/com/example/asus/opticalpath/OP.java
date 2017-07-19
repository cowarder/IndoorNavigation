package com.example.asus.opticalpath;

import android.graphics.Point;

import java.util.Arrays;
import java.util.Collection;
import java.util.Comparator;

/**
 * Created by ASUS on 2017/7/19.
 */

public class OP {

    public OP(Vertex start,Vertex end){
        s=start;
        e=end;
    }

    public void shortestPath(){
        int knownVertex=1;
        Vertex v=s;
        v.distance=0;
        for( ; ; ){
            Arrays.sort(MainActivity.Bag, new MyComprator());    //使用指定的排序器，进行排序
            for(int i=0;i!=MainActivity.Bag.length;i++){        //找出未知且到开始结点距离最小的点
                if(MainActivity.Bag[i].known==false){
                    v=MainActivity.Bag[i];
                }
            }

            v.known=true;
            for(Vertex item:v.adjVertex){
                if(!item.known){
                    if(v.distance+1<item.distance){
                        item.distance=v.distance+1;
                        item.previous=v;
                        for(Vertex p:MainActivity.Bag){
                            if(item.xCoordinate==p.xCoordinate&&item.yCoordinate==p.yCoordinate) {
                                p=item;
                            }
                        }
                    }
                }
            }
        }
    }
    private final Vertex s;
    private final Vertex e;

    class MyComprator implements Comparator {           //从小到大进行排序的规则
        public int compare(Object arg0, Object arg1) {
            Vertex t1=(Vertex) arg0;
            Vertex t2=(Vertex) arg1;
            return t1.distance>t2.distance? 1:-1;
        }
    }
}
