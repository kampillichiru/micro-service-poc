
import { combineLatest, of } from 'rxjs';
import { switchMap, map, toArray } from 'rxjs/operators';

// ...

getOcrizedFileAndClassifications(ocrRequest: OcrRequest): Observable<[Observable<File>, DocumentClassification[]]> {
  return this.documentocrRepository.prepareOcrRequestsTest(ocrRequest).pipe(
    switchMap(splittedFilesArray => {
      const ocrizedFilesObservable: Observable<File>[] = [];
      const classificationsObservable: Observable<DocumentClassification[]>[] = [];

      splittedFilesArray.forEach(splittedFile => {
        const ocrizedFileObservable = this.documentocrRepository.ocrizeDocument({
          file: splittedFile,
          isOCRRequiredPageByPage: ocrRequest.isOCRRequiredPageByPage,
          isPageLevelOCRizationFeatureOn: ocrRequest.isPageLevelOCRizationFeatureOn
        });

        ocrizedFilesObservable.push(ocrizedFileObservable);

        const classificationsObservableForFile = ocrizedFileObservable.pipe(
          switchMap(ocrizedFile =>
            this.documentClassificationRepository.getClassifications(ocrizedFile)
          )
        );

        classificationsObservable.push(classificationsObservableForFile);
      });

      const combinedObservable = combineLatest([...ocrizedFilesObservable, ...classificationsObservable]).pipe(
        map(results => {
          const ocrizedFiles: Observable<File>[] = results.slice(0, splittedFilesArray.length);
          const documentclassifications: DocumentClassification[] = results.slice(splittedFilesArray.length).flat();

          const mergedFileObservable = combineLatest(ocrizedFiles).pipe(
            switchMap(ocrizedFilesArray => OcrPdfUtils.mergeFiles(ocrizedFilesArray)),
            map(pdfBlob => new File([pdfBlob], ocrRequest.file.name))
          );

          return [mergedFileObservable, documentclassifications];
        })
      );

      return combinedObservable;
    })
  );
}
