name: PR Triggered Build and Snapshot Upload

on:
  pull_request:
    types: [opened, synchronize]

jobs:
  build-and-upload-snapshot:
    runs-on: ubuntu-latest

    steps:
      - name: Checkout code
        uses: actions/checkout@v2

      - name: Set up JDK 8
        uses: actions/setup-java@v1
        with:
          java-version: 1.8

      - name: Build with Maven
        run: mvn clean install -B

      - name: Upload Snapshot to Nexus
        run: |
          mvn deploy -DskipTests \
            -Dmaven.javadoc.skip=true \
            -Dnexus.username=$NEXUS_USERNAME \
            -Dnexus.password=$NEXUS_PASSWORD \
            -Dnexus.url=$NEXUS_URL \
            -DskipStaging=true \
            -Dmaven.deploy.skip=true \
            -DaltDeploymentRepository=snapshot-repo::default::${NEXUS_URL}/repository/maven-snapshots/

        env:
          NEXUS_USERNAME: ${{ secrets.NEXUS_USERNAME }}
          NEXUS_PASSWORD: ${{ secrets.NEXUS_PASSWORD }}
          NEXUS_URL: https://your-nexus-url









name: Manual Release Build and Deploy

on:
  workflow_dispatch:

jobs:
  release-and-deploy:
    runs-on: ubuntu-latest

    steps:
      - name: Checkout code
        uses: actions/checkout@v2

      - name: Set up JDK 8
        uses: actions/setup-java@v1
        with:
          java-version: 1.8

      - name: Set up Git
        run: git config --global user.email "actions@github.com" && git config --global user.name "GitHub Actions"

      - name: Determine next version and release
        id: next-version
        run: |
          current_version=$(mvn help:evaluate -Dexpression=project.version -q -DforceStdout)
          next_version=$(echo $current_version | sed 's/-SNAPSHOT//')
          echo "::set-output name=next_version::$next_version"

      - name: Release version and deploy to Nexus
        run: |
          mvn versions:set -DnewVersion=${{ steps.next-version.outputs.next_version }} \
            && mvn clean deploy -DskipTests \
            -Dmaven.javadoc.skip=true \
            -Dnexus.username=$NEXUS_USERNAME \
            -Dnexus.password=$NEXUS_PASSWORD \
            -Dnexus.url=$NEXUS_URL \
            -DskipStaging=true \
            -Dmaven.deploy.skip=true \
            -DaltDeploymentRepository=release-repo::default::${NEXUS_URL}/repository/maven-releases/

        env:
          NEXUS_USERNAME: ${{ secrets.NEXUS_USERNAME }}
          NEXUS_PASSWORD: ${{ secrets.NEXUS_PASSWORD }}
          NEXUS_URL: https://your-nexus-url

      - name: Commit and push version change
        run: |
          git commit -am "Prepare release ${{ steps.next-version.outputs.next_version }}"
          git push
