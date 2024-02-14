import { TestBed } from '@angular/core/testing';
import { TestWorkSpaceService } from './document-workspace.service';

describe('TestWorkSpaceService', () => {
  let service: TestWorkSpaceService;

  beforeEach(() => {
    TestBed.configureTestingModule({
      providers: [TestWorkSpaceService]
    });
    service = TestBed.inject(TestWorkSpaceService);
  });

  it('should be created', () => {
    expect(service).toBeTruthy();
  });

  describe('addDocument', () => {
    it('should add a document correctly', async () => {
      // Mock file and documentIndex
      const file = new File(['file content'], 'filename.pdf');
      const documentIndex = 0;

      await service.addDocument(file, documentIndex);

      expect(service.documents.mergedDocument.length).toBe(1);
      expect(service.documents.mergedDocument[0].fileName).toBe('filename.pdf');
      // Add more expectations as needed
    });
    // Add more test cases for error handling, etc.
  });

describe('my arrangment', () => {
  it('should ', () => {
    // Mock data
    const classifications = [
      { pageNumber: 1, indexCode: 'A' },
      { pageNumber: 2, indexCode: 'B' },
    ];

    // Mock mergedDocument
    service.documents.mergedDocument = [
      { documentNumber: 1, startPageNumber: 1, endPageNumber: 2 }
    ];

    const rearrangedClassifications = service.reArrangeClassifications(classifications);

    // Add expectations to verify the rearrangement
  });

  it('should  1', () => {
    // Mock data
    const classifications = [
      { pageNumber: 1, indexCode: 'A' },
      { pageNumber: 2, indexCode: 'B' },
      // Add more classifications as needed
    ];

    // Mock mergedDocument
    service.documents.mergedDocument = [
      { documentNumber: 1, startPageNumber: 1, endPageNumber: 2 },
      { documentNumber: 2, startPageNumber: 3, endPageNumber: 4 },
      // Add more mergedDocument entries as needed
    ];

    const rearrangedClassifications = service.reArrangeClassifications(classifications);

    // Add expectations to verify the rearrangement
  });

  it('should', () => {
    // Mock data
    const classifications = [];

    // Mock mergedDocument
    service.documents.mergedDocument = [
      { documentNumber: 1, startPageNumber: 1, endPageNumber: 2 }
    ];

    const rearrangedClassifications = service.reArrangeClassifications(classifications);

    // Add expectations to verify the behavior when classifications is empty
  });
});


  // Add test cases for other methods like setStartAndEndPageNumbersToDocument, updateIndexesCodes, getUniqueCategories
});
