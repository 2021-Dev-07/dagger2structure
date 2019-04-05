package cof.gof.dagger2structure.diModules;

import cof.gof.dagger2structure.shared.User;
import dagger.Module;
import dagger.Provides;

@Module
public class UserModule {

    User user;

    public  UserModule(User temp){
        user = temp;
    }

    @Provides
    User providesUser(){
        return  user;
    }
}
