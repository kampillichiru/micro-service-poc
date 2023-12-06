const observables = from(splittedFilesArray).pipe(
  mergeMap((splittedFile) =>
    this.documentocrRepository
      .isOCRRequiredPageByPage(ocrRequest.isOCRRequiredPageByPage)
      .isPageLevelOCRizationFeatureOn(ocrRequest.isPageLevelOCRizationFeatureOn)
      .pipe(
        mergeMap(() => this.ocrizeDocument({ file: splittedFile })),
        switchMap((ocrizedFile) =>
          forkJoin([of(ocrizedFile), this.documentclassificationRepository.getClassifications(ocrizedFile)])
        )
      )
  ),
  toArray()
);
