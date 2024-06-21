name: Update and Release Version

on:
  push:
    tags:
      - 'v*.*.*'  # Triggers the workflow on version tags

jobs:
  release:
    runs-on: ubuntu-latest

    steps:
      - name: Checkout Repository
        uses: actions/checkout@v2

      - name: Set up JDK 11
        uses: actions/setup-java@v3
        with:
          distribution: 'adopt'
          java-version: '11'

      - name: Set Release Version
        run: mvn versions:set -DnewVersion=${GITHUB_REF#refs/tags/} -DgenerateBackupPoms=false

      - name: Commit Release Version
        run: |
          git config --global user.name 'github-actions'
          git config --global user.email 'github-actions@github.com'
          git add pom.xml
          git commit -m "Set release version ${GITHUB_REF#refs/tags/}"

      - name: Push Changes
        env:
          GITHUB_TOKEN: ${{ secrets.GITHUB_TOKEN }}
        run: git push origin HEAD:main

      - name: Deploy to Maven Repository
        run: mvn deploy -DskipTests
        env:a
          GPG_PRIVATE_KEY: ${{ secrets.GPG_PRIVATE_KEY }}
          GPG_PASSPHRASE: ${{ secrets.GPG_PASSPHRASE }}
          OSSRH_USERNAME: ${{ secrets.OSSRH_USERNAME }}
          OSSRH_PASSWORD: ${{ secrets.OSSRH_PASSWORD }}



name: Extract Release Version Workflow

on:
  push:
    branches:
      - main  # Adjust to your main branch name

jobs:
  extract-release-version:
    runs-on: ubuntu-latest

    steps:
      - name: Checkout Repository
        uses: actions/checkout@v2

      - name: Set up JDK and Maven
        uses: actions/setup-java@v3
        with:
          java-version: '11'  # Adjust to your Java version requirement

      - name: Extract Version from pom.xml
        id: extract_version
        run: |
          VERSION=$(mvn help:evaluate -Dexpression=project.version -q -DforceStdout)
          echo "::set-output name=version::$VERSION"

      - name: Remove -SNAPSHOT suffix
        id: remove_snapshot_suffix
        run: |
          RELEASE_VERSION=${{ steps.extract_version.outputs.version }}
          RELEASE_VERSION=${RELEASE_VERSION%-SNAPSHOT}
          echo "::set-output name=release_version::$RELEASE_VERSION"

      - name: Use Release Version
        run: echo "Release version is ${{ steps.remove_snapshot_suffix.outputs.release_version }}"
