import { ComponentFixture, TestBed } from '@angular/core/testing';
import { ChangeRequestComponent } from './change-request.component';
import { ReleaseFacade } from 'path-to-your-release-facade';
import { ApplicationNavigationService } from 'path-to-your-application-navigation-service';
import { FormsModule } from '@angular/forms';
import { DatePipe } from '@angular/common';
import { ElementRef } from '@angular/core';
import { of } from 'rxjs';

describe('ChangeRequestComponent', () => {
  let component: ChangeRequestComponent;
  let fixture: ComponentFixture<ChangeRequestComponent>;
  let releaseFacadeMock: Partial<ReleaseFacade>;
  let applicationNavigationServiceMock: Partial<ApplicationNavigationService>;
  let datePipeMock: Partial<DatePipe>;

  beforeEach(async () => {
    releaseFacadeMock = {
      getRelease: () => of({ id: 'release-id' }) // mock the getRelease method
    };

    applicationNavigationServiceMock = {
      navigateToReleasePageById: jasmine.createSpy('navigateToReleasePageById')
    };

    datePipeMock = {
      transform: (value: any) => value // mock the transform method
    };

    await TestBed.configureTestingModule({
      declarations: [ ChangeRequestComponent ],
      imports: [ FormsModule ],
      providers: [
        { provide: ReleaseFacade, useValue: releaseFacadeMock },
        { provide: ApplicationNavigationService, useValue: applicationNavigationServiceMock },
        { provide: DatePipe, useValue: datePipeMock }
      ]
    })
    .compileComponents();
  });

  beforeEach(() => {
    fixture = TestBed.createComponent(ChangeRequestComponent);
    component = fixture.componentInstance;
    fixture.detectChanges();
  });

  it('should create', () => {
    expect(component).toBeTruthy();
  });

  it('should create change request', () => {
    // Simulate form validity
    const form = {
      valid: true,
      value: {
        changeModel: 'changeModel',
        itServiceName: 'itServiceName',
        impactedLocations: 'impactedLocations',
        impactedCoreBusinesses: 'impactedCoreBusinesses',
        summary: 'summary',
        description: 'description',
        assignedTo: 'assignedTo',
        assignmentGroup: 'assignmentGroup',
        shortDescription: 'shortDescription'
      }
    };

    // Simulate native elements
    component.startDate = new ElementRef({ value: '2024-02-18' });
    component.endDate = new ElementRef({ value: '2024-02-19' });
    component.startDateTime = new ElementRef({ value: '08:00' });
    component.endDateTime = new ElementRef({ value: '17:00' });

    // Mocking releaseFacade method
    spyOn(component.releaseFacade, 'createChange').and.returnValue(of(true));
    spyOn(component.releaseFacade, 'isChangeRequestSuccessful').and.returnValue(of(true));

    component.createChange(form as any);

    // Verify if the methods are called
    expect(component.releaseFacade.createChange).toHaveBeenCalled();
    expect(component.releaseFacade.isChangeRequestSuccessful).toHaveBeenCalled();
    expect(component.applicationNavigationService.navigateToReleasePageById).toHaveBeenCalled();
    expect(component.isChangeRequestRunning).toBeTruthy();
  });
});
v 
