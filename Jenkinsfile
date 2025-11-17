def s_branch = env.BRANCH_NAME as String
def registry = "containerregistry.spot-me-app.com/spotme/" as String
def localRegistry = "containerregistry.magus.lab" as String
def localRegistryUrl = "containerregistry.magus.lab" as String
def registryUrl = "http://containerregistry.magus.lab" as String
def registryBase = "containerregistry.spot-me-app.com" as String
def appName = "spotme-rest-svc" as String
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
                withCredentials([string(credentialsId: 'spotme-rest-jwt-jenkins', variable: 'JWT_SECRET_KEY')]) {
                    sh 'echo "Using API Key: $API_KEY"'
                    sh ''' chmod +x mvnw '''
                    sh ''' ./mvnw clean install -ntp -Dmaven.test.skip '''
                }                
            }
        }
        stage("Test"){
            steps{
                withCredentials([string(credentialsId: 'spotme-rest-jwt-jenkins', variable: 'JWT_SECRET_KEY')]) {
                        sh ''' ./mvnw --batch-mode test '''
                }
            }
        }
        stage('SonarQube Analysis') {
            steps {
                withCredentials([string(credentialsId: 'spotme-rest-jwt-jenkins', variable: 'JWT_SECRET_KEY')]) {
                    withSonarQubeEnv('Sonarqube') {
                    sh "./mvnw clean verify sonar:sonar -Dsonar.projectKey='${appName}' -Dsonar.projectName='${appName}' -Dsonar.branch.name=${env.BRANCH_NAME}"
                }
                }   
            }
        }

        stage("Store Artifacts"){
            steps{
               archiveArtifacts artifacts: 'target/*.jar', followSymlinks: false
            }
        }

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
    }
    post {
           always{
                cleanWs()
           }
    }
}
