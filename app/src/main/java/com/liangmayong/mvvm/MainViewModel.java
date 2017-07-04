package com.liangmayong.mvvm;

import com.liangmayong.mvvm.core.ViewModel;
import com.liangmayong.mvvm.core.ViewModelInterface;

/**
 * Created by LiangMaYong on 2017/7/4.
 */

public class MainViewModel extends ViewModel {

    public MainViewModel(ViewModelInterface viewModelInterface) {
        super(viewModelInterface);
    }

    public User user = new User();

    public String buttonText = "Button";

    /**
     * Created by LiangMaYong on 2017/7/4.
     */

    public static class User {

        public String name = "1111111";
        public String age = "1111111";
        public Text text = new Text();


        public static class Text{
           public String text = "nihao";
        }
    }
}