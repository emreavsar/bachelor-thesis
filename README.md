# QUALI-T: Quality Attribute Elicitation Tool for Architectural Analysis
Status: [![Build Status](https://travis-ci.org/team-qualit/quali-t.svg?branch=master)](https://travis-ci.org/team-qualit/quali-t)

Bachelor thesis for IFS Institute for Software (www.ifs.hsr.ch)
Authors: Corina Honegger (c1honegg@hsr.ch), Emre Avsar (emre@avsar.ch)

## Getting stared
### Checkout project
```sh
TODO: git checkout
```

### Run application
The application can be run in two modes (dev and prod). In production mode all assets (images, css, javascript) are minified. Also hot-deployment is disabled. TODO: THIS IS NOT DONE YET

Start app in prod mode
```sh
activator run -Dconfig.file=./conf/prod.conf
```
Start app in test mode
```sh
activator run
```
> PS: Play has problems with activator start, somehow it does not work with the prod.conf file
> Use always activator *run*

## Development
You can start developing this software by follow the instructions below:

TODO: Improve
Basic steps will be
- checkout this git repo
- startup vagrant with provisioning
- open your browser on http://192.168.50.4:9000/

## Travis CI
Both frontend and backend applications are integrated in Travis CI

## Latest build
TODO: Link to heroku, when automatic cloud deployment is ready.

## Contact & License
ask eavsar@hsr.ch and c1honegg@hsr.ch