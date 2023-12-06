import { forkJoin, of } from 'rxjs';
import { switchMap, map, toArray } from 'rxjs/operators';

// ...

getOcrizedFileAndClassifications(ocrRequest: OcrRequest): Observable<[Observable<File>, DocumentClassification[]]> {
  return this.documentocrRepository.prepareOcrRequestsTest(ocrRequest).pipe(
    switchMap(splittedFilesArray => {
      const observables: Observable<[Observable<File>, DocumentClassification[]]>[] = [];

      splittedFilesArray.forEach(splittedFile => {
        const ocrizedFileObservable = this.documentocrRepository.ocrizeDocument({
          file: splittedFile,
          isOCRRequiredPageByPage: ocrRequest.isOCRRequiredPageByPage,
          isPageLevelOCRizationFeatureOn: ocrRequest.isPageLevelOCRizationFeatureOn
        });

        const classificationsObservable = ocrizedFileObservable.pipe(
          switchMap(ocrizedFile =>
            forkJoin([of(ocrizedFile), this.documentClassificationRepository.getClassifications(ocrizedFile)])
          )
        );

        observables.push(classificationsObservable);
      });

      return forkJoin(observables).pipe(
        map(results => {
          const ocrizedFiles: Observable<File>[] = results.map(([ocrizedFileObservable, _]) => ocrizedFileObservable);
          const documentclassifications: DocumentClassification[] = results.flatMap(([_, classifications]) => classifications);

          const mergedFileObservable = forkJoin(ocrizedFiles).pipe(
            switchMap(ocrizedFilesArray => OcrPdfUtils.mergeFiles(ocrizedFilesArray)),
            map(pdfBlob => new File([pdfBlob], ocrRequest.file.name))
          );

          return [mergedFileObservable, documentclassifications];
        })
      );
    })
  );
}
