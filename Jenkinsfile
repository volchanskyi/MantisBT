node {
  stage ('Checkout') {
  git url: 'https://github.com/volchanskyi/TwoLevelFramework.git'
}

  stage ('build') {
  docker.build('selenium_base')
  }
  
  stage ('deploy') {
  sh './deploy.sh'
  }
}