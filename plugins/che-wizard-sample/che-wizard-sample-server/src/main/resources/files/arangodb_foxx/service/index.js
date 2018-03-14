'use strict';
const createRouter = require('@arangodb/foxx/router');
const router = createRouter();

module.context.use(router);
router.get('/hello-world', function (req, res) {
  res.send('Hello World!');
})
.response(['text/plain'], 'A generic greeting.')
.summary('Generic greeting')
.description('Prints a generic greeting.');


/*
 
 
To initialize git, and git deploy, you have to execute these commands into the che terminal. 
This will initialize your project with git, and save your credentials so the upload can be automated (Must be github)

git init
git add *
git commit -am "Initial"
git remote add origin *YOUR_GIT_URL_HERE*
git config credential.helper store
git push -u origin master


RUN COMMAND
 

cd ${current.project.path} 
git commit -am "deploy"  
git push  
curl -X PUT -H "Content-Type: application/json" -d '{"appInfo" : "git:farleon/foxxtest:master", "mount" : "/my-mount-point112"}' "http://root:KAc9Veam2soRgjHt+CXNYHeO@10.10.138.70:8529/_db/_system/_admin/foxx/install" 

 

cd ${current.project.path} 
git commit -am "deploy"  
git push  
curl -X PUT -H "Content-Type: application/json" -d '{"appInfo" : "YOUR_GIT_URL_HERE", "mount" : "/YOUR_MOUNT_POINT_HERE"}' "http://ARANGODB_USER:ARANGODB_PASS@ARANGODB_IP/_db/_system/_admin/foxx/install" 

 */