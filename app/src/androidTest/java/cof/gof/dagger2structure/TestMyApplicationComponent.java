package cof.gof.dagger2structure;

import cof.gof.dagger2structure.MainActivity.TestInstrumentMainActivity;
import dagger.Component;
import dagger.Module;
import dagger.android.AndroidInjectionModule;

@Component(
        modules = {
                AndroidInjectionModule.class,
                TestBindingModule.class,
                TestMyApplicationComponent.TestAppModule.class
        })
interface TestMyApplicationComponent {

    void inject(TestMyApplication app);

    @Module
    class TestAppModule {
    }
}