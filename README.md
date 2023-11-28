import { Observable, from } from 'rxjs';
import { map, switchMap } from 'rxjs/operators';

prepareocrRequests(ocrRequest: OcrRequest): Observable<OcrRequest[]> {
  return DocumentocrMapper.splitFileByPages(ocrRequest).pipe(
    map((requestFileBlobs: Blob[]) => {
      const ocrReqPdfFiles: OcrRequest[] = [];

      requestFileBlobs.forEach((blob) => {
        ocrReqPdfFiles.push({
          file: new File([blob], ocrRequest.file.name),
          isOCRRequiredPageByPage: ocrRequest.isOCRRequiredPageByPage,
          isPageLevelOCRizationFeatureOn: ocrRequest.isPageLevelOCRizationFeatureon,
        });
      });

      return ocrReqPdfFiles;
    })
  );
}
