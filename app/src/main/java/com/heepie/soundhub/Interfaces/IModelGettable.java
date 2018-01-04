package com.heepie.soundhub.Interfaces;

/**
 * Created by Heepie on 2017. 11. 25..
 * 제네릭을 사용하기 위해 생성
 * Adapter에서 Model를 get하기 위해 Holder에서 사용
 * ViewModel에서 구현
 */

public interface IModelGettable<T> {
    T getModel();
}
