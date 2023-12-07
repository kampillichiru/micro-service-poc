
const combinedObservable = forkJoin(ocrizedFilesObservable).pipe(
        mergeMap(ocrizedFiles => {
          const classificationsObservable: Observable<DocumentClassification[]>[] = ocrizedFiles.map(ocrizedFile =>
            this.documentClassificationRepository.getClassifications(ocrizedFile).pipe(
              map(classifications => ({ ocrizedFile, classifications }))
            )
          );

          return forkJoin(classificationsObservable);
        }),
