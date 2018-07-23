package com.github.mproberts.rxdatabindingdemo.home.activity;

import android.os.Bundle;

import com.github.mproberts.rxdatabinding.notifications.NotificationBindingHandler;
import com.github.mproberts.rxdatabindingdemo.R;
import com.github.mproberts.rxdatabindingdemo.di.AndroidBindingMainActivity;
import com.github.mproberts.rxdatabindingdemo.home.HomeModule;
import com.github.mproberts.rxdatabindingdemo.home.vm.HomeViewModel;
import com.github.mproberts.rxdatabindingdemo.home.vm.NotificationViewModel;
import com.github.mproberts.rxtools.list.SimpleFlowableList;

import io.reactivex.functions.Action;

public class MainActivity extends AndroidBindingMainActivity {

    @Override
    protected int getLayoutResource() {
        return R.layout.activity_main;
    }

    @Override
    public HomeViewModel navigateToHome() {
        return getDomainComponent()
                .plus(new HomeModule())
                .viewModel();
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        NotificationBindingHandler<NotificationViewModel> notifications = new NotificationBindingHandler<>(
                "com.github.mproberts.rxdatabindingdemo",
                this);
        SimpleFlowableList<NotificationViewModel> demoList = new SimpleFlowableList<>();

        demoList.add(new NotificationViewModel(1, "Hello", "World"));

        notifications.setCreator(new NotificationBindingHandler.NotificationCreator<NotificationViewModel>() {
            @Override
            public void createNotification(NotificationViewModel model, NotificationBindingHandler.NotificationBinding binding) {
                binding.setNotificationId(model.notificationId());
                binding.setContentTitle(model.title());
                binding.setContentText(model.subtitle());
                binding.getBuilder().setSmallIcon(R.drawable.ic_notification);
                binding.setTapActionListener(MainActivity.class, new Action() {
                    @Override
                    public void run() throws Exception {
                        model.onItemTapped();
                    }
                });
            }
        });
        notifications.setList(demoList);
    }

    @Override
    protected boolean showBackButton() {
        return false;
    }
}
