import { Session } from "../features/sessions/interfaces/session.interface";
import { SessionInformation } from "../interfaces/sessionInformation.interface";
import { Teacher } from "../interfaces/teacher.interface";
import { User } from "../interfaces/user.interface";

export const mockBaseUser : User = {
    id: 1,
    email: 'email@email.com',
    lastName: 'lastname',
    firstName: 'firstname',
    admin: false,
    password: 'password',
    createdAt: new Date(),
    updatedAt: new Date(),
}

export const mockSessionInformationAdmin : SessionInformation = {
    token: `e85c9ffdaeff0bf290b2eebffd25ff56255d0a2c163edf229ebb83b189334962
    22724c1dd101ed52d9d88ea1c71eab235a7a4dbd380539b5e779373627460acc
    09193a12b1e67899ad9c16ebf95df5a5ba15e4ac2f546d4780283caecabc6bbf
    6d431d8ac22a9895182017951cb17af4e8ce14ee68353be337803f60999558d2
    ebf88d87131f7f8e4641d0a16ac0f81a2ee807d7b6384fe0c2023acd925e51dc
    abd55b2f56bfb5ec5ca4e44e64cb02976adc3fbeaf60ff7d6a808fe3f1b5954b
    01bbafcda59eb4c4ada6c1af90eef515c5f32b44d3bcea1f3641ea9664324e1f
    18e124861170470ba6d707bf0cb778975d0307caf2761ebcf0ec50cea8d52e56
    4203a428e662e69f4129c8dfde2a2bf5aff449bb2d6beaaf032d7778d665da5a
    789f2ed26aaed7dbe298b48d0e8c0420743bf8f880025cfdf43a3ba64b03765e`,
    type: 'type',
    id: 1,
    username: 'username',
    firstName: 'firstname',
    lastName: 'lastname',
    admin: true
}

export const mockYogaSession1 : Session = {
    id : 1,
    name : 'name1',
    description : 'description1',
    date : new Date("10/10/2023"),
    teacher_id : 1,
    users : [2, 3],
    createdAt : new Date(),
    updatedAt : new Date(),
}

export const mockYogaSession2 : Session = {
    id : 2,
    name : 'name2',
    description : 'description2',
    date : new Date("12/10/2023"),
    teacher_id : 2,
    users : [4, 5],
    createdAt : new Date(),
    updatedAt : new Date(),
}
  
export const mockTeacher1 : Teacher = {
    id: 1,
    lastName: "lastname1",
    firstName: "firstname1",
    createdAt: new Date(),
    updatedAt: new Date(),
}

export const mockTeacher2 : Teacher = {
    id: 2,
    lastName: "lastname2",
    firstName: "firstname2",
    createdAt: new Date(),
    updatedAt: new Date(),
}