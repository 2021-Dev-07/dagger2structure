package cof.gof.dagger2structure.mainActivity.di;

import cof.gof.dagger2structure.diModules.UserModule;
import cof.gof.dagger2structure.mainActivity.MainActivity;
import cof.gof.dagger2structure.shared.User;
import cof.gof.dagger2structure.testModules.FlombulatorTestModule;
import cof.gof.dagger2structure.testModules.UserTestModule;
import dagger.BindsInstance;
import dagger.Module;
import dagger.Subcomponent;
import dagger.android.AndroidInjector;

@Subcomponent(modules = {
        FlombulatorTestModule.class,
        UserTestModule.class,
        TestMainActivitySubComponent.MainActivityModule.class
})
public interface TestMainActivitySubComponent extends AndroidInjector<MainActivity> {

    @Module
    class MainActivityModule {}

    @Subcomponent.Builder
    abstract class Builder extends AndroidInjector.Builder<MainActivity> {
        public abstract void setUserModule(UserTestModule module);

        @Override
        public void seedInstance(MainActivity instance) {
            User user = new User(instance.getNameForUser());
            setUserModule(new UserTestModule(user));
            bindMainActivity(instance);
        }
        @BindsInstance
        public abstract void bindMainActivity(MainActivity mainActivity);
    }
}
