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
	- postgresql
- run db creation sql
```sh
sudo -u postgres < quali-t-app/conf/1_0.sql
```
- start backend application 
- open your browser on http://localhost:9000/

# Travis CI
Both frontend and backend applications are integrated in Travis CI

# Latest build
https://quali-t.herokuapp.com/

# FAQ (Development)

#### **How can I add new initial data?**

Initial data is inserted through `import.sql` file located in `conf` directory.
Important: Do not use multiline sql imports, since this is not supported by default.

## Contact & License
ask eavsar@hsr.ch and c1honegg@hsr.ch