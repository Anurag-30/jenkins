library identifier: 'sharedlibrary@master', // sharedlibrary name can be anything , master refers to the branch it has to be a valid one
    retriever: modernSCM([
      $class: 'GitSCMSource',
      credentialsId: 'GIT_AUTH', //Credentials from Jenkins
      remote: '<GIT_URL>'
    ])

env.SERVICE = 'name-of-the-service'  // setting environment variables in Jenkins

Buildpipeline ([:])