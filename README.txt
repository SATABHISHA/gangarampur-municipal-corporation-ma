Git global setup
=================
git config --global user.name "Satabhisha Roy"
git config --global user.email "satabhisha.innits@gmail.com"

Create a new repository
========================
git clone https://gitlab.com/Satabhisha/doctor-booking.git
cd doctor-booking
touch README.md
git add README.md
git commit -m "add README"
git push -u origin master

Existing folder
====================
cd existing_folder
git init
git remote add origin https://gitlab.com/Satabhisha/doctor-booking.git
git pull origin master
git add .
git commit -m "Initial commit"
git push -u origin master

Existing Git repository
===========================
cd existing_repo
git remote rename origin old-origin
git remote add origin https://gitlab.com/Satabhisha/doctor-booking.git
git push -u origin --all
git push -u origin --tags

git remote set 


SSH issues:
===========
E:\gt\doctor-booking>git push
remote: You are not allowed to push code to this project.
fatal: unable to access 'https://gitlab.com/Satabhisha/doctor-booking.git/': The requested URL returned error: 403

E:\gt\doctor-booking>git remote set-url origin ssh://gitlab.com/Satabhisha/doctor-booking.git/

E:\gt\doctor-booking>git remote -v
origin  ssh://gitlab.com/Satabhisha/doctor-booking.git/ (fetch)
origin  ssh://gitlab.com/Satabhisha/doctor-booking.git/ (push)

E:\gt\doctor-booking>git pull
The authenticity of host 'gitlab.com (52.167.219.168)' can't be established.
ECDSA key fingerprint is SHA256:HbW3g8zUjNSksFbqTiUWPWg2Bq1x8xdGUrliXFzSnUw.
Are you sure you want to continue connecting (yes/no)? yes
Warning: Permanently added 'gitlab.com,52.167.219.168' (ECDSA) to the list of known hosts.
Permission denied (publickey).
fatal: Could not read from remote repository.

Please make sure you have the correct access rights
and the repository exists.

E:\gt\doctor-booking>git pull
Permission denied (publickey).
fatal: Could not read from remote repository.

Please make sure you have the correct access rights
and the repository exists.


//-----------to add Package.Swift following are the commands----------
1)swift package init(run in terminal at root folder of the project)
2)add package in Package.Swift
3)swift build(in terminal)
4)swift package generate-xcodeproj(in terminal)
