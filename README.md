    - name: Set up Maven settings.xml
      run: |
        mkdir -p ~/.m2
        cp .github/settings.xml.template ~/.m2/settings.xml
        sed -i 's/\${NEXUS_USERNAME}/'"${{ secrets.NEXUS_USERNAME }}"'/' ~/.m2/settings.xml
        sed -i 's/\${NEXUS_PASSWORD}/'"${{ secrets.NEXUS_PASSWORD }}"'/' ~/.m2/settings.xml
