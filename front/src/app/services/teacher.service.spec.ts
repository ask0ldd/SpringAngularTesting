import { HttpClient, HttpClientModule } from '@angular/common/http';
import { TestBed } from '@angular/core/testing';
import { expect } from '@jest/globals';

import { TeacherService } from './teacher.service';
import { Teacher } from '../interfaces/teacher.interface';
import { of } from 'rxjs';
import { mockTeacher1, mockTeacher2 } from '../testing/mockDatas';

const teacher1 : Teacher = {...mockTeacher1}

const teacher2 : Teacher = {...mockTeacher2}

describe('TeacherService', () => {
  let service: TeacherService;
  let httpClient: HttpClient;
  const pathService = 'api/teacher'

  beforeEach(() => {
    TestBed.configureTestingModule({
      imports:[
        HttpClientModule
      ]
    });
    service = TestBed.inject(TeacherService);
    httpClient = TestBed.inject(HttpClient)
    jest.resetAllMocks()
  });

  it('should be created', () => {
    expect(service).toBeTruthy();
  });

  // --------
  // Get all Teachers / Unit Test
  // --------

  describe('when the all method is called', () => {
    it('should send a request to the expected endpoint & sreturn an observable broadcasting an array of teachers', () => {
      const targetEndpoint = `${pathService}`

      jest.spyOn(httpClient, 'get').mockReturnValue(of([{...teacher1}, {...teacher2}]))

      // !!! evaluations into a subscribe callback are ignored
      let subResponse
      service.all().subscribe((response) => {
        subResponse = response
      })
      expect(httpClient.get).toHaveBeenCalledWith(targetEndpoint)
      expect(JSON.stringify(subResponse)).toEqual(JSON.stringify([{...teacher1}, {...teacher2}]))
    })
  })

  // --------
  // Get some teachers details / Unit Test
  // --------

  describe('when the detail method is called with a teacher id passed to it', () => {
    it('should send a request to the expected endpoint & return an observable broadcasting the infos of the target teacher', () => {
      const teacherId = '1'
      const targetEndpoint = `${pathService}/${teacherId}`

      jest.spyOn(httpClient, 'get').mockReturnValue(of(teacher1))

      // !!! evaluations into a subscribe callback are ignored
      let subResponse
      service.detail(teacherId).subscribe((response) => {
        subResponse = response
      })
      expect(httpClient.get).toHaveBeenCalledWith(targetEndpoint)
      expect(JSON.stringify(subResponse)).toEqual(JSON.stringify(teacher1))
    })
  })

});
