// CODE_CHANGES = getGItChanges()
pipeline {

  agent any
  // tools {
  //   maven 'Maven'
  // }
  // environment {
  //   NEW_VERSION = '1.3.0'
  //   // SERVER_CREDENTIALS = credentials('server-credentials')
  // }
  parameters {
    // string(name: 'VERSION', defaultValue: '', description: 'version to deploy on prod')
    choice(name: 'VERSION', choices: ['1.1.0', '1.2.0', '1.3.0'], description: '')
    booleanParam(name: 'execcuteTests', defaultValue: true, description: '') 
  stages {
    
    stage("build") {
      // when {
      //   expression {
      //     BRANCH_NAME == 'dev' && CODE_CHANGES == true
      //   }
      // }     
      steps {
        echo 'building the application...'
        // echo "building version ${NEW_VERSION}"
        // sh "mvn install"
      }
    }

    stage("test") {
      // when {
      //   expression {
      //     BRANCH_NAME == 'dev' || BRANCH_NAME == 'master'
      //   }
      // }
      when {
        expression {
          params.executeTests
        }
      }
      steps {
        echo 'testing the application...'
      }
    }

    stage("deploy") {
      
      steps {
        echo 'deploying the application...'
        // echo "deploying with ${SERVER_CREDENTIALS}"
        // sh "${SERVER_CREDENTIALS}"
        echo "deploying version ${params.VERSION}"
        // withCredentials([
        //   usernamePassword(credentials: 'server-credentials', usernameVariable: USER, passwordVariable: PWD)
        // ]){
        //   sh "some script ${USER} ${PWD}"
        // }

      }
    }
  }
  // post {
  //   always {
  //     // 
  //   }
  //   success {

  //   }
  //   failure {

  //   }
  // }
}

// node {
//   //groovy script
// }
 
