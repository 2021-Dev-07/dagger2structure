package cof.gof.dagger2structure.testModules;

import cof.gof.dagger2structure.shared.User;
import dagger.Module;
import dagger.Provides;

@Module
public class UserFakeModule {

    User mUser;
    public UserFakeModule(User user){
        mUser = user;
    }

    @Provides
    User providesUser(){
        return mUser;
    }
}