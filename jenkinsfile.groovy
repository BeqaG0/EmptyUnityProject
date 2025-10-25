pipeline {
    agent any

    environment {
        UNITY_PATH = "A:/Unity/6000.0.60f1/Editor./Unity.exe"      // Path to Unity executable
        PROJECT_PATH = "${WORKSPACE}"               // Unity project root
        BUILD_PATH = "${WORKSPACE}/Build"           // Output build folder
        BUILD_TARGET = "StandaloneWindows64"        // Change as needed (e.g., StandaloneOSX, Android, iOS, etc.)
        BUILD_NAME = "MyGame.exe"                   // Output file name
        LOG_FILE = "${WORKSPACE}/unity_build.log"
    }

    stages {
        stage('Checkout') {
            steps {
                checkout scm
            }
        }

        stage('Unity Build') {
            steps {
                script {
                    sh """
                        chmod +x "${UNITY_PATH}"
                        "${UNITY_PATH}" \
                            -quit -batchmode -nographics \
                            -projectPath "${PROJECT_PATH}" \
                            -buildTarget ${BUILD_TARGET} \
                            -executeMethod BuildScript.PerformBuild \
                            -logFile "${LOG_FILE}" || exit 1
                    """
                }
            }
        }

        stage('Archive Build') {
            steps {
                archiveArtifacts artifacts: 'Build/**/*.*', fingerprint: true
            }
        }
    }

    post {
        always {
            echo 'Cleaning up...'
            sh 'rm -rf Library/Temp'
            archiveArtifacts artifacts: 'unity_build.log', onlyIfSuccessful: false
        }
    }
}
