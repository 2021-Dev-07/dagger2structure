# dagger2structure
Dagger2Structure is a demo app to understand implementation of dagger-android and testing

## Dagger-2
----------------------------------------------------------------------------------------------
Dagger is a dependency Injection library which can inject dependencies from outside the class.


This Document will discuss about some of the annotations and analysis of the generated code by dagger.

Dagger2 contains a dependency graph which define the dependency for classes.
```
@Component
@Modules
@provides
@Scope
```
These are the major annotation defined by dagger to create boilerplate code needed for dependency 
We will discuss about the structure of the auto-generated classes by dagger to understand 
Code behind the screen, which may help us to use dagger efficiently.

@Component interface will act as the top class in dependency graph. This will create class implementing Component interface and its abstract methods.

###### Structure of a component class:
```
Class component{
has provider instance variables those will provide dependencies annotated by @provides.
has a builder class().
	-builder class will initialize modules. 
	-builder class will have a build method which will initialize component with modules 
has initialize method ()
	-This will be called from component constructor and initialize all provide instances
	 With use of modules.
has inject methods.
	- once developer called inject () methods, components will inject those instance and all 
	 @inject annotated variables in the instance will get initialized.
	 
```
So, from the above structure it’s clear that every class should call inject () method to initialize its @inject annotated instances. 
Since the build () method provides instance of Dagger Component, to set getter methods, we need to add those into the component interface and those will be implemented as a public methods in Dagger Component class.


## Dagger@-Android for android specific classes.
Dagger-android library defined for android specific classes like Application, Activity, Fragment etc. Dagger-android consider components are the application components and Activity, Fragments should have subcomponents. Component and subcomponents have the same structure defined before. But Subcomponents act as modules in Components.

```
@ContributeAndroidInjector
@Binds
```

There are the  additional annotations used in android-dagger library.

AndroidInjector class has a major role in android-dagger to implement subcomponent concept and to inject activity/fragment instances.
We can discuss from top to bottom of android-dagger implementation. This will help us to clearly understand and implement dagger in efficient manner. 

> Application Component > Activity Component > Fragment Component.

Structure of application component:

Class component structure is same in  android-dagger and dagger2.
Only difference we can see that, the subcomponents added as a provider instance in component. 
Subcomponent provider provides an android injector class for the respective activity and this will help us to inject instance into the activity.

For understand details, just look into androidInjector.class. 
Structure of Android Injector class :
```
androidInjector{
-has a factory 
-builder extends from factory creates class having implementation of android Injector. 
}
```

We have seen before that component has a provider instance.
```
Compoent{
-has provider instance.
-has initialize methods which will initialize providers. [here providers are androidInjection class]
}
```

DaggerApplication class have some instance which need to be injected. 

-activityInjector
-fragmentInjector ect…

Component class will inject andoridInject provider instance into application activityInjector. 
This activity injector has the main role to find the respective activity subcomponent implemention and inject instance into the respective activity.

###### Behind the screen :

When we call androiddInjector(this), dagger will find activityInjector from the application class and find the respective subcomponent  provider to initialize instances. Once provider received, dagger inject “this” into the component and initialize @inject annotated instances. Same way fragment works. Please look into the boilerplate code and find the implementation in detail.

This is a document intended to share some basic idea about dagger2- and dagger-android injections. 


## Dagger-Android Test

###### Overview:

Dagger-Android has introduced with intension of writing dependency injection graph in efficient way based on android framework. Android has different lifecycle classes like Application, Fragment, Activity. All these classes are related as below.
Application (top class) > Activity > Fragment > Nested Fragment 
So, dagger-android suggested to implement dependency graph in the same way.   
Component (for application) > Subcomponent (For activity & Fragment)
Subcomponents have the same structure as components explained in dagger-2. But only a small difference that, subcomponents are the subclasses of androidInjector class which will help us to inject dependencies to activity.

[Demo Project](https://github.com/roshanstephen/dagger2structure)

###### Unit test:

As per google Document, for a single class we don’t need to use DI for testing. Since classes are already decoupled using dagger injection, developer can easily test single classes with help of Junit

Same as explained in google:
```
final class ThingDoer {
  private final ThingGetter getter;
  private final ThingPutter putter;

  @Inject ThingDoer(ThingGetter getter, ThingPutter putter) {
    this.getter = getter;
    this.putter = putter;
  }

  String doTheThing(int howManyTimes) { /* … */ }
}

public class ThingDoerTest {
  @Test
  public void testDoTheThing() {
    ThingDoer doer = new ThingDoer(fakeGetter, fakePutter);
    assertEquals("done", doer.doTheThing(5));
  }
}
```


###### UI Test:
Android UI test can be done using Espresso or RoboElectric. But this time we should know how to how the dependencies are injected into UI or Activity.
As we discussed before, Android has different lifecycle components. As per dagger, all activities and fragment’s subcomponents are initialized along with ApplicationComponent(Dagger component). So before launching activity for test, we should teach the app component how the dependencies are provided, that means we can provide mock dependencies to the component. 

As per the suggestions from google and other related documents, we can write a test component with test subcomponent and other fake modules and teach dagger to use these classes for creating a dependency graph for testing. If you thing too much complicated, please look at the Demo project 
Demo : https://github.com/roshanstephen/dagger2structure.

I have written UI test using espresso and Roboelectric. Both you can find in androidTest and Test folders respectively.

Code for Test component :
```
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
```
Here TestBindingModule.class defines Fake subcompoents and its dependencies. Please look at the class. 

```
@Module
(subcomponents ={TestMainActivitySubComponent.class,SecondaryActivitySubComponent.class})

public abstract class TestBindingModule 
{

    @Binds
    @IntoMap
    @ActivityKey(MainActivity.class)
    abstract AndroidInjector.Factory<? extends Activity> bindMainActivityInjectorFactory(TestMainActivitySubComponent.Builder builder);

    @Binds
    @IntoMap
    @ActivityKey(SecondaryActivity.class)
    abstract AndroidInjector.Factory<? extends Activity> bindMainSecondaryInjectorFactory(SecondaryActivitySubComponent.Builder builder);

@ContributesAndroidInjector(modules = FlombulatorModule.class)
abstract ThirdActivity bindThirdActivity();

}
```

Have you see a difference the way creating subcomponents.  For First two activities, I created a subcomponentbuilder and binds with andoridInjector but for the last one, I didn’t.  why because, dagger-android provides a new annotation called @ContributesAndroidInjectior to generate all boilerplate code for you like subclass for androidInjector and builder class. We can create subcomponent this way when we don’t need any dependencies at run time. Since autogenerated builder class initialize the dependency by itself. If you need a dependency at runtime, choose the second option. For reference I have created activities in both ways and shown how to provide dependency at runtime. Please have a look at MainActivitySubcomponent.class.

Once we done with Fake components and Modules , create a Test application and build testComponent as shown below.
```
public class TestMyApplication extends Application implements HasActivityInjector {
    @Inject
    DispatchingAndroidInjector<Activity> dispatchingActivityInjector;


    @Override
    public void onCreate() {
        super.onCreate();
        System.out.println("HEY IM UP TESTING APPLICATION COMPONENT ==========================+>");
        //DaggerTestApplicationComponent.create().inject(this);
        DaggerTestMyApplicationComponent.create().inject(this);
    }

    @Override
    public DispatchingAndroidInjector<Activity> activityInjector() {
        return dispatchingActivityInjector;
    }
}
```
Not over.  Once you done with your test application, tell AndroidJunitRunner to call our test application instead to initialize androidTest. For that, create a mokRunner class and define like this.
```
public class MockTestRunner extends AndroidJUnitRunner {

    @Override
    public Application newApplication(ClassLoader cl, String className, Context context)
            throws InstantiationException, IllegalAccessException, ClassNotFoundException {
        return super.newApplication(cl, TestMyApplication.class.getName(), context);
    }
}
```

Now tell gradle to use MockTestRunner as instrumentationTestrunner:

> testInstrumentationRunner "cof.gof.dagger2structure.MockTestRunner"

That’s It !..Now you can launch activity using Espresso or Roboelectric and do the test 

Espresso test class:
```
@RunWith(AndroidJUnit4.class)
public class TestInstrumentMainActivity {

    @Rule
    public ActivityTestRule<MainActivity> activityRule = new ActivityTestRule<>(
            MainActivity.class,
            true,     // initialTouchMode
            false);   // launchActivity. False so we can customize the intent per test method



    @Before
    public void setUp() throws Exception {
    }

    @Test
    public void addition_isCorrect() throws Exception {

        activityRule.launchActivity(new Intent());
        MainActivity mainActivity = activityRule.getActivity();

        assertEquals("flumbolate test", mainActivity.flumbolate());
    }

    @Test
    public void isViewShowing() throws  Exception{
        activityRule.launchActivity(new Intent());
        onView(withId(R.id.textView)).check(matches(withText("flumbolate test")));
    }


}
```

RoboElectric test class:
```
@Config(constants = BuildConfig.class, sdk = 21, application = TestApplication.class)
@RunWith(RobolectricTestRunner.class)
public class TestMainActivity {

    private MainActivity mainActivity;

    @Before
    public void setUp() throws Exception {
    }

    @Test
    public void addition_isCorrect() throws Exception {
        mainActivity = Robolectric.buildActivity(MainActivity.class).create().visible().get();

        assertEquals("flumbolate test", mainActivity.flumbolate());
    }

    @Test
    public void getName_isCorrect() throws  Exception{
        mainActivity = Robolectric.buildActivity(MainActivity.class).create().visible().get();
        assertEquals("Roshan",mainActivity.getUserName());
    }

}
```
Done!  You gone through most of the aspects of dagger implementation and i believe got the basic understanding. Now try yourself .

Thanks 

###### Reference
https://medium.com/@harivigneshjayapalan/dagger-2-for-android-beginners-introduction-be6580cb3edb
https://google.github.io/dagger/android#injecting-activity-objects
https://google.github.io/dagger/users-guide
https://proandroiddev.com/how-to-dagger-2-with-android-part-2-10f4fb8f62d0
https://medium.com/@fabioCollini/android-testing-using-dagger-2-mockito-and-a-custom-junit-rule-c8487ed01b56
