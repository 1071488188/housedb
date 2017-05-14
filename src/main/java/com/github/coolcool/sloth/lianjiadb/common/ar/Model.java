package com.github.coolcool.sloth.lianjiadb.common.ar;

import java.util.Map;

/**
 * Created by dee on 2017/3/16.
 */
public abstract class Model < M extends  Model> {

    public M put(String key, Object value){

        return (M)this;
    }

}
