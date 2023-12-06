import { concatMap, map } from 'rxjs/operators';

// ...

getMyResr(ocrRequest: OcrRequest): Observable<{ ocrizedFile: File, classifications: DocumentClassification[] }[]> {
  const splittedFiles: Observable<File[]> = this.documentocrRepository.ocrizeDocumentTwo(ocrRequest);

  return splittedFiles.pipe(
    concatMap(splittedFilesArray => {
      const observables: Observable<{ ocrizedFile: File, classifications: DocumentClassification[] }>[] = [];

      splittedFilesArray.forEach(splittedFile => {
        const ocrizeDocumentObservable = this.documentocrRepository.ocrizeDocument(splittedFile);

        // Use concatMap to process each file sequentially
        const classificationsObservable = ocrizeDocumentObservable.pipe(
          concatMap(ocrizedFile =>
            this.documentclassificationRepository.getclassifications(ocrizedFile).pipe(
              map(classifications => ({ ocrizedFile, classifications }))
            )
          )
        );

        // Combine the observable for each file
        observables.push(classificationsObservable);
      });

      // Combine all observables into a sequential stream
      return forkJoin(observables);
    }),
    finalize(() => this.updateLoadingStatusGamma(...this.documents)),
    takeUntil(this.ngDestroyed$)
  );
}
