def s_branch = env.BRANCH_NAME as String
def registry = "containerregistry.spot-me-app.com/spotme/" as String
def localRegistry = "http://192.168.1.227:8082/" as String
def localRegistryUrl = "http://192.168.1.227:8082" as String
def registryUrl = "https://containerregistry.spot-me-app.com" as String
def registryBase = "containerregistry.spot-me-app.com" as String
def appName = "spotme-auth-svc" as String
s_branch = s_branch.replaceAll("/","_")

pipeline{
    agent any
    stages{
        stage("Clean Up"){
            steps{
                deleteDir()
            }
        }
        stage("Clone repo"){
            steps{
                checkout scm
            }
        }

        stage("Build"){
            steps{
                sh ''' chmod +x mvnw '''
                sh ''' ./mvnw clean install -ntp -Dmaven.test.skip '''
            }
        }
        stage("Test"){
            steps{
               dir("./"){
               sh ''' ./mvnw --batch-mode test '''
              }
            }
        }
        stage('SonarQube Analysis') {
            steps {
                withSonarQubeEnv('Sonarqube') {
                    sh "./mvnw clean verify sonar:sonar -Dsonar.projectKey='${appName}' -Dsonar.projectName='${appName}'"
            }
            }
        }
         stage("Quality Gate") {
            steps {
              timeout(time: 1, unit: 'HOURS') {
                waitForQualityGate abortPipeline: true
              }
            }
          }
        stage("Store Artifacts"){
            steps{
               archiveArtifacts artifacts: 'target/*.jar', followSymlinks: false
            }
        }
    //    stage("Build Container Images"){
    //        steps(){
    //            dir("spotme-rest/"){
    //            sh """ docker build -t ${registry}spotme-rest:${s_branch} . """
    //            }
    //            dir("${appName}/"){
    //            sh """ docker build -t ${registry}${appName}:${s_branch} . """
    //            }
    //        }
    //    }
        stage("Image Upload"){
            steps(){
                script{
                    dir("./"){
                        try{
                            echo 'Tunnel URL did not work for image push, trying to push via intranet'
                            docker.withRegistry(localRegistryUrl,'spotme-containerregistry') {

                                def smweb_l = docker.build("spotme/${appName}:${s_branch}","./")

                                // or docker.build, etc.
                                def remoteImage = "${registryBase}/spotme/${appName}:${s_branch}"
                                smweb_l.push()
                                sh "echo LOCAL_IMAGE_NAME=${smweb_l.imageName()} >> pipeline.properties"
                                sh "echo LOCAL_IMAGE_NAME=${smweb_l.imageName()} >> imageRef.properties"
                                sh "echo IMAGE_NAME=${remoteImage} >> pipeline.properties"
                                sh "echo IMAGE_NAME=${remoteImage} >> imageRef.properties"
                            }
                        }catch(e){
                            docker.withRegistry(registryUrl,'spotme-containerregistry') {
                                // sh "docker system prune -a -f"

                                def smweb = docker.build("spotme/${appName}:${s_branch}","./")
                                //"docker push ${registry}${appName}:${s_branch}"

                                // or docker.build, etc.
                                sh "echo IMAGE_NAME=${smweb.imageName()} >> pipeline.properties"
                                sh "echo IMAGE_NAME=${smweb.imageName()} >> imageRef.properties"
                                smweb.push()
                                // echo DOCKER_IMAGE_NAME='''+image_name+''' > pipeline.properties
                                
                            }
                        }
                    }

                }
            }
        }
    
        stage("Store Pipeline Artifacts"){
            steps{
               archiveArtifacts artifacts: 'imageRef.properties', followSymlinks: false
               archiveArtifacts artifacts: 'pipeline.properties', followSymlinks: false
            }
        }
//        stage("Deploy") {
//            steps {
//                script{
//                    if(env.BRANCH_NAME=="develop"){
//                        dir("kube/") {
//                            script {
//                                try {
//                                    sh """kubectl delete -k overlays/dev/ --force"""
//                                } catch (e) {
//                                    println e
//                                    sh '''echo "Was not able delete old resources"'''
//                                }
//                                try {
//                                    sh """kubectl apply -k overlays/dev/"""
//                                    sh """kubectl rollout restart -k  overlays/dev/"""
//                                } catch (e) {
//                                    println e
//                                    sh '''echo "Was not able to start web service, might be running already"'''
//                                }
//                            }
//                        }
//
//                    }else if(env.BRANCH_NAME=="master"){
//                        dir("kube/") {
//                            script {
//                                try {
//                                    sh """kubectl delete -k overlays/test/ --force"""
//                                } catch (e) {
//                                    println e
//                                    sh '''echo "Was not able delete old resources"'''
//                                }
//                                try {
//                                    sh """kubectl apply -k overlays/test/"""
//                                    sh """kubectl rollout restart -k  overlays/test/"""
//                                } catch (e) {
//                                    println e
//                                    sh '''echo "Was not able to start web service, might be running already"'''
//                                }
//                            }
//                        }
//
//                    }else if(env.BRANCH_NAME=="release"){
//                        dir("kube/") {
//                            script {
//                                try {
//                                    sh """kubectl delete -k overlays/prod/ --force"""
//                                } catch (e) {
//                                    println e
//                                    sh '''echo "Was not able delete old resources"'''
//                                }
//                                try {
//                                    input message: 'Deploy to Production?', ok: 'Deploy', parameters: [string(defaultValue: 'hotfix', description: 'This is necessary to make sure we are intentional when deploying to production', name: 'Release Number')]
//                                    sh """kubectl apply -k overlays/prod/"""
//                                } catch (e) {
//                                    println e
//                                    sh '''echo "Was not able to start web service, might be running already"'''
//                                }
//                            }
//                        }
//                    }
//            }
//
//            }
//
//        }

/////-------- OLD REMOTE UPLOAD METHOD -------/////
                        // dir("./"){
// {
//                             docker.withRegistry(registryUrl,'spotme-containerregistry') {
//                                 // sh "docker system prune -a -f"

//                                 def smweb = docker.build("spotme/${appName}:${s_branch}","./")
//                                 //"docker push ${registry}${appName}:${s_branch}"

//                                 // or docker.build, etc.
//                                 sh "echo IMAGE_NAME=${smweb.imageName()} >> pipeline.properties"
//                                 sh "echo IMAGE_NAME=${smweb.imageName()} >> imageRef.properties"
//                                 smweb.push()
//                                 // echo DOCKER_IMAGE_NAME='''+image_name+''' > pipeline.properties
                                
//                             }
//                         }




    }
    post {
           always{
                cleanWs()
           }
    }
}

// node {
//     // some block
//     git branch: env.BRANCH_NAME, url: 'git@github.com:SpotMe-Mondayvile/spotme.git'
//
// sh '''
// cd ${appName}
// npm install
// npm install -g serve
// npm run build
// tar -czvf ${appName}-archive.tar.gz build
// serve -s build
// cd ..'''
//
// sh '''
// cd spotme-rest
// mvn package -ntp -Dmaven.test.skip
// '''
//
// def projectVersion = sh script: "cd spotme-rest && mvn -q -Dexec.executable=echo -Dexec.args='\${project.version}' --non-recursive exec:exec", returnStdout: true
//
// sh'''cd ..'''
// archiveArtifacts artifacts: '${appName}/${appName}-archive.tar.gz*', followSymlinks: false
// archiveArtifacts artifacts: 'spotme-rest/target/*.jar', followSymlinks: false
//
// // sh '''docker image ls'''
// }
