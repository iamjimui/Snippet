import { TestBed } from '@angular/core/testing';

import { AceEditorService } from './ace-editor.service';

describe('AceEditorService', () => {
  let service: AceEditorService;

  beforeEach(() => {
    TestBed.configureTestingModule({});
    service = TestBed.inject(AceEditorService);
  });

  it('should be created', () => {
    expect(service).toBeTruthy();
  });
});
