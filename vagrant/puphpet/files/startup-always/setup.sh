#!/bin/bash

# vars
activator_version=1.3.2
activator_download_url_min="http://downloads.typesafe.com/typesafe-activator/$activator_version/typesafe-activator-$activator_version-minimal.zip"
vagrant_home="/home/vagrant"
project_home="/quali-t-app"

# sudo apt-get update
# sudo apt-get upgrade
# . ~/.bashrc
# sudo wget -qO- https://toolbelt.heroku.com/install-ubuntu.sh | sh

# Required for SASS
sudo `which gem` install compass

# save pgpassword in environment
echo "source PGPASSWORD=123" >> /home/vagrant/.bashrc

sql -U postgres -h localhost -c "ALTER USER qualitapp CREATEDB;"

# Download activator
cd $vagrant_home
sudo wget $activator_download_url_min
sudo unzip $vagrant_home/typesafe-activator-$activator_version-minimal.zip 
sudo chmod +x -R $vagrant_home/activator-$activator_version-minimal/*
sudo rm $vagrant_home/typesafe-activator-$activator_version-minimal.zip*

# add activator to path
sudo echo "export PATH=$vagrant_home/activator-$activator_version-minimal:\$PATH" >> $vagrant_home/.bashrc

# fix inotify watchers
echo 524288 | sudo tee -a /proc/sys/fs/inotify/max_user_watches

#reset bash
. $vagrant_home/.bashrc