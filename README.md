name: Release

on:
  push:
    branches:
      - main

jobs:
  release:
    name: Release Workflow
    runs-on: ubuntu-latest

    steps:
      - name: Checkout repository
        uses: actions/checkout@v2

      - name: Set up Java
        uses: actions/setup-java@v2
        with:
          distribution: 'adopt'
          java-version: '11'  # Specify your Java version

      - name: Push release version to Nexus
        run: |
          mvn clean deploy -DskipTests --settings .maven/nexus-settings.xml
        env:
          NEXUS_USERNAME: ${{ secrets.NEXUS_USERNAME }}
          NEXUS_PASSWORD: ${{ secrets.NEXUS_PASSWORD }}

      - name: Update release version in pom.xml
        run: |
          RELEASE_VERSION=$(mvn help:evaluate -Dexpression=project.version -q -DforceStdout | sed 's/-SNAPSHOT//')
          mvn versions:set -DnewVersion=$RELEASE_VERSION
          git config --global user.name "github-actions"
          git config --global user.email "github-actions@github.com"
          git add pom.xml
          git commit -m "Release version $RELEASE_VERSION"
          git tag -a "v$RELEASE_VERSION" -m "Release version $RELEASE_VERSION"
          git push origin --tags

      - name: Update to next snapshot version
        run: |
          RELEASE_VERSION=$(mvn help:evaluate -Dexpression=project.version -q -DforceStdout | sed 's/-SNAPSHOT//')
          SNAPSHOT_VERSION=$(echo $RELEASE_VERSION | awk -F. '{printf "%d.%d.%d-SNAPSHOT", $1, $2, $3+1}')
          mvn versions:set -DnewVersion=$SNAPSHOT_VERSION
          git add pom.xml
          git commit -m "Update to next snapshot version $SNAPSHOT_VERSION"
          git push origin main

      - name: Push snapshot version to Nexus
        run: |
          mvn deploy -DskipTests --settings .maven/nexus-settings.xml
        env:
          NEXUS_USERNAME: ${{ secrets.NEXUS_USERNAME }}
          NEXUS_PASSWORD: ${{ secrets.NEXUS_PASSWORD }}
