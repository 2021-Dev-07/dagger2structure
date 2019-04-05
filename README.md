# dagger2structure
Dagger2Structure is a demo app to understand implementation of dagger-android and testing


Dagger is a dependency Injection library which can inject dependencies from outside the class.

Details can be find in below link.
Link: https://medium.com/@harivigneshjayapalan/dagger-2-for-android-beginners-introduction-be6580cb3edb
This Document will discuss about some of the annotations and analysis of the generated code by dagger.

Dagger2 contains a dependency graph which define the dependency for classes.
@Component
@Modules
@provides
@Scope
These are the major annotation defined by dagger to create boilerplate code needed for dependency 
We will discuss about the structure of the auto-generated classes by dagger to understand 
Code behind the screen, which may help us to use dagger efficiently.

@Component interface will act as the top class in dependency graph. This will create class implementing Component interface and its abstract methods.
Structure of a component class:
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

So, from the above structure it’s clear that every class should call inject () method to initialize its @inject annotated instances. 
Since the build () method provides instance of Dagger Component, to set getter methods, we need to add those into the component interface and those will be implemented as a public methods in Dagger Component class.

Dagger@-Android for android specific classes.
Dagger-android library defined for android specific classes like Application, Activity, Fragment etc. Dagger-android consider components are the application components and Activity, Fragments should have subcomponents. Component and subcomponents have the same structure defined before. But Subcomponents act as modules in Components.
@ContributeAndroidInjector
@Binds
There are the  additional annotations used in android-dagger library.
AndroidInjector class has a major role in android-dagger to implement subcomponent concept and to inject activity/fragment instances.
We can discuss from top to bottom of android-dagger implementation. This will help us to clearly understand and implement dagger in efficient manner. 
Application Component > Activity Component > Fragment Component.
Structure of application component:
Class component structure is same in  android-dagger and dagger2.
Only difference we can see that, the subcomponents added as a provider instance in component. 
Subcomponent provider provides an android injector class for the respective activity and this will help us to inject instance into the activity.
For understand details, just look into androidInjector.class. 
Structure of Android Injector class :
androidInjector{
-has a factory 
-builder extends from factory creates class having implementation of android Injector. 
}
We have seen before that component has a provider instance.
Compoent{
-has provider instance.
-has initialize methods which will initialize providers. [here providers are androidInjection class]
}
DaggerApplication class have some instance which need to be injected. 
-activityInjector
-fragmentInjector ect…
Component class will inject andoridInject provider instance into application activityInjector. 
This activity injector has the main role to find the respective activity subcomponent implemention and inject instance into the respective activity.

Behind the screen :
When we call androiddInjector(this), dagger will find activitInjector from the application class and find the respective subcomponent  provider to initialize instances. Once provider received, dagger inject “this” into the component and initialize @inject annotated instances.
Same way fragment works. Please look into the boilerplate code and find the implementation in detail.

This is a document intended to share some basic idea about dagger2- and dagger-android injections. 
