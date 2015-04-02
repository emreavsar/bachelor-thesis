# QUALI-T: Quality Attribute Elicitation Tool for Architectural Analysis
Status: [![Build Status](https://travis-ci.org/team-qualit/quali-t.svg?branch=master)](https://travis-ci.org/team-qualit/quali-t)

Bachelor thesis for IFS Institute for Software (www.ifs.hsr.ch)

Authors: Corina Honegger (c1honegg@hsr.ch), Emre Avsar (emre@avsar.ch)

# Getting stared
## Checkout project
```sh
git clone git@github.com:team-qualit/quali-t.git
```

## Run application
The application can be run in two modes (dev and prod). In production mode all assets (images, css, javascript) are minified. Also hot-deployment is disabled.

Start app in **prod** mode
```sh
activator run -Dconfig.file=./conf/prod.conf
```
Start app in **test** mode
```sh
activator run
```
> PS: Play has problems with activator start, somehow it does not work with the prod.conf file
> Use always activator *run*

# Development
You can start developing this software by follow the instructions below:

- checkout this git repo
- install prerequisite software
	- TODO write which sw
- build frontend app
	- grunt
- open your browser on http://localhost:9000/

# Travis CI
Both frontend and backend applications are integrated in Travis CI

# Latest build
https://quali-t.herokuapp.com/

# FAQ (Development)

#### **How can I add new initial data?**

Initial data gets inserted when the application gets started. Adding data is easily done in the `conf/initial-data.yml`.
When you add new types of ressources, you must handle it in `Global.java`.

**Example:**

Imagine you created a new model type `app/modles/mypackage/MyModel`

````java
@Entity
@Table(name = "\"mymodel\"")
@Nullable
public class MyModel extends AbstractEntity {
    private String name;

    // getter and setter
}
````
You'll add these lines in the `conf/initial-data.yml` file: 

````yml
mymodels:
    - !!models.mypackage.MyModel
        name:            myfirstmodel
    - !!models.mypackage.MyModel
        name:            mysecondmodel
````

Since this is a new resource type (`MyModel` didn't exist before, so quali-t does not now it) you'll update the `Global.java`'s `onStart()` method:

````java
@Override
public void onStart(Application app) {
    try {
        JPA.withTransaction("default", false, () -> {
	           	// put some sample data from initial-data.yml
	           	Map<String, List<Object>> all 
	           		= (Map<String, List<Object>>) Yaml.load("initial-data.yml");

        	resetMyModels(all.get("mymodels"); // <-- your new function
        });
    } catch (Throwable throwable) {
        play.Logger
        	.error("Error at initializing database with conf/initial-data.yml file");
    }
}

private void resetMyModels(List<Object> mymodels) {
    MyModelDao myModelDao = new MyModelDao();
    myModelDao.removeAll();

    for (Object mym : mymodels) {
        MyModel casted = (MyModel) mym;
        MyModel merged = JPA.em().merge(casted);
    }
}
````


## Contact & License
ask eavsar@hsr.ch and c1honegg@hsr.ch