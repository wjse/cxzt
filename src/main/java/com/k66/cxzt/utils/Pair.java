package com.k66.cxzt.utils;

import lombok.Data;

@Data
public class Pair<L , R> {
    L left;
    R right;

    public static <L , R> Pair<L , R> of(L left , R right){
        Pair<L , R> p = new Pair<>();
        p.left = left;
        p.right = right;
        return p;
    }
}
